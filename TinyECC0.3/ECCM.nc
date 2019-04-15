/**
 * All new code in this distribution is Copyright 2005 by North Carolina
 * State University. All rights reserved. Redistribution and use in
 * source and binary forms are permitted provided that this entire
 * copyright notice is duplicated in all such copies, and that any
 * documentation, announcements, and other materials related to such
 * distribution and use acknowledge that the software was developed at
 * North Carolina State University, Raleigh, NC. No charge may be made
 * for copies, derivations, or distributions of this material without the
 * express written consent of the copyright holder. Neither the name of
 * the University nor the name of the author may be used to endorse or
 * promote products derived from this material without specific prior
 * written permission.
 *
 * IN NO EVENT SHALL THE NORTH CAROLINA STATE UNIVERSITY BE LIABLE TO ANY
 * PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF THE NORTH CAROLINA STATE UNIVERSITY HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. THE SOFTWARE PROVIDED HEREUNDER IS ON AN
 * "AS IS" BASIS, AND THE NORTH CAROLINA STATE UNIVERSITY HAS NO
 * OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR
 * MODIFICATIONS. "
 *
 */

/**
 * EccM, the module implement Ecc operations
 *
 * Author: An Liu
 * Date: 09/29/2006
 */

includes NN;
includes ECC;

//enable mixed projective coordinate addition
#define ADD_MIX
//enable repeated point doubling
#define REPEAT_DOUBLE

module ECCM {
  provides interface ECC;
  uses interface NN;
  uses interface CurveParam;
}

implementation {
  //parameters for ECC operations
  Params param;
  //precomputed array for base point
  Point pBaseArray[NUM_POINTS];
  //masks for sliding window method
  NN_DIGIT mask[NUM_MASKS];

  void c_add_projective(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2, NN_DIGIT * Z2);
  void c_add_mix(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2);
  void c_dbl_projective(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1);

  
  // test whether the ith bit in a is one
  NN_DIGIT b_testbit(NN_DIGIT * a, int16_t i)
  {

    return (*(a + (i / NN_DIGIT_BITS)) & ((NN_DIGIT)1 << (i % NN_DIGIT_BITS)));
  }
    
  // set P0's x and y to zero
  void p_clear(Point * P0)
  {
    call NN.AssignZero(P0->x, NUMWORDS);
    call NN.AssignZero(P0->y, NUMWORDS);
  }

  // P0 = P1
  void p_copy(Point * P0, Point * P1)
  {
    call NN.Assign(P0->x, P1->x, NUMWORDS);
    call NN.Assign(P0->y, P1->y, NUMWORDS);
  }

  // test whether x and y of P0 is all zero
  bool p_iszero(Point * P0)
  {
    bool result = FALSE;
    
    if (call NN.Zero(P0->x, NUMWORDS))
      if (call NN.Zero(P0->y, NUMWORDS))
        result = TRUE;
    return result;
  }

  // test whether points P1 and P2 are equal
  bool p_equal(Point * P1, Point * P2)
  {
    if (call NN.Equal(P1->x, P2->x, NUMWORDS))
      if (call NN.Equal(P1->y, P2->y, NUMWORDS))
        return TRUE;
    return FALSE;
  }

  // test whether Z is one
  bool Z_is_one(NN_DIGIT *Z)
  {
    uint8_t i;
    
    for (i = 1; i < NUMWORDS; i++)
      if (Z[i])
        return FALSE;
    if (Z[0] == 1)
      return TRUE;
    
    return FALSE;
  }

  // Point addition, P0 = P1 + P2
  void c_add(Point * P0, Point * P1, Point * P2)
  {
    NN_DIGIT Z0[NUMWORDS];
    NN_DIGIT Z1[NUMWORDS];
    NN_DIGIT Z2[NUMWORDS];
    
    p_clear(P0);
    call NN.AssignZero(Z0, NUMWORDS);
    call NN.AssignZero(Z1, NUMWORDS);
    call NN.AssignZero(Z2, NUMWORDS);
    Z1[0] = 0x01;
    Z2[0] = 0x01;

#ifdef ADD_MIX
    c_add_mix(P0, Z0, P1, Z1, P2);
#else
    c_add_projective(P0, Z0, P1, Z1, P2, Z2);
#endif

    if (!Z_is_one(Z0))
    {
      call NN.ModInv(Z1, Z0, param.p, NUMWORDS);
      call NN.ModMultOpt(Z0, Z1, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->x, P0->x, Z0, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(Z0, Z0, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->y, P0->y, Z0, param.p, param.omega, NUMWORDS);
    }

  }

  void c_add_mix(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2)
  {
    NN_DIGIT t1[NUMWORDS];
    NN_DIGIT t2[NUMWORDS];
    NN_DIGIT t3[NUMWORDS];
    NN_DIGIT t4[NUMWORDS];
    NN_DIGIT Z2[NUMWORDS];

    //P2 == infinity
    if (call NN.Zero(P2->x, NUMWORDS)){
      if (call NN.Zero(P2->y, NUMWORDS)){
	p_copy(P0, P1);
	call NN.Assign(Z0, Z1, NUMWORDS);
	return;
      }
    }
    
    //P1 == infinity
    if (call NN.Zero(Z1, NUMWORDS)){
      p_copy(P0, P2);
      call NN.AssignDigit(Z0, 1, NUMWORDS);
      return;
    }

    //T1 = Z1^2
    call NN.ModSqrOpt(t1, Z1, param.p, param.omega, NUMWORDS);
    //T2 = T1*Z1
    call NN.ModMultOpt(t2, t1, Z1, param.p, param.omega, NUMWORDS);
    //T1 = T1*P2->x
    call NN.ModMultOpt(t1, t1, P2->x, param.p, param.omega, NUMWORDS);
    //T2 = T2*P2->y
    call NN.ModMultOpt(t2, t2, P2->y, param.p, param.omega, NUMWORDS);
    //T1 = T1-P1->x
    call NN.ModSub(t1, t1, P1->x, param.p, NUMWORDS);
    //T2 = T2-P1->y
    call NN.ModSub(t2, t2, P1->y, param.p, NUMWORDS);
    
    if (call NN.Zero(t1, NUMWORDS)){
      if (call NN.Zero(t2, NUMWORDS)){
	call NN.AssignDigit(Z2, 1, NUMWORDS);
	c_dbl_projective(P0, Z0, P2, Z2);
	return;
      }else{
	call NN.AssignDigit(Z0, 0, NUMWORDS);
	return;
      }
    }
    //Z3 = Z1*T1
    call NN.ModMultOpt(Z0, Z1, t1, param.p, param.omega, NUMWORDS);
    //T3 = T1^2
    call NN.ModSqrOpt(t3, t1, param.p, param.omega, NUMWORDS);
    //T4 = T3*T1
    call NN.ModMultOpt(t4, t3, t1, param.p, param.omega, NUMWORDS);
    //T3 = T3*P1->x
    call NN.ModMultOpt(t3, t3, P1->x, param.p, param.omega, NUMWORDS);
    //T1 = 2*T3
    call NN.LShift(t1, t3, 1, NUMWORDS);
    call NN.ModSmall(t1, param.p, NUMWORDS);
    //P0->x = T2^2
    call NN.ModSqrOpt(P0->x, t2, param.p, param.omega, NUMWORDS);
    //P0->x = P0->x-T1
    call NN.ModSub(P0->x, P0->x, t1, param.p, NUMWORDS);
    //P0->x = P0->x-T4
    call NN.ModSub(P0->x, P0->x, t4, param.p, NUMWORDS);
    //T3 = T3-P0->x
    call NN.ModSub(t3, t3, P0->x, param.p, NUMWORDS);
    //T3 = T3*T2
    call NN.ModMultOpt(t3, t3, t2, param.p, param.omega, NUMWORDS);
    //T4 = T4*P1->y
    call NN.ModMultOpt(t4, t4, P1->y, param.p, param.omega, NUMWORDS);
    //P0->y = T3-T4
    call NN.ModSub(P0->y, t3, t4, param.p, NUMWORDS);

    return;
  }

  // (P0,Z0) = (P1,Z1) + (P2,Z2)  in Jacobian projective coordinate
  // P0, P1, P2 can be same pointer
  void c_add_projective(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2, NN_DIGIT * Z2)
  {
    NN_DIGIT n0[NUMWORDS];
    NN_DIGIT n1[NUMWORDS];
    NN_DIGIT n2[NUMWORDS];
    NN_DIGIT n3[NUMWORDS];
    NN_DIGIT n4[NUMWORDS];
    NN_DIGIT n5[NUMWORDS];
    NN_DIGIT n6[NUMWORDS];

    if (call NN.Zero(Z1, NUMWORDS))
    {
      p_copy(P0, P2);
      call NN.Assign(Z0, Z2, NUMWORDS);
      return;
    }

    if (call NN.Zero(Z2, NUMWORDS))
    {
      p_copy(P0, P1);
      call NN.Assign(Z0, Z1, NUMWORDS);
      return;
    }
    
    //double
    if (p_equal(P1, P2))
    {
      c_dbl_projective(P0, Z0, P1, Z1);
      return;
    }
    
    //add_proj
    //n1, n2
    if (Z_is_one(Z2))
    {
      // n1 = P1->x, n2 = P1->y
      call NN.Assign(n1, P1->x, NUMWORDS);
      call NN.Assign(n2, P1->y, NUMWORDS);
    }
    else
    {
      //n1 = P1->x * Z2^2
      call NN.ModSqrOpt(n0, Z2, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(n1, P1->x, n0, param.p, param.omega, NUMWORDS);
      //n2 = P1->y * Z2^3
      call NN.ModMultOpt(n0, n0, Z2, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(n2, P1->y, n0, param.p, param.omega, NUMWORDS);
    }
    
    // n3, n4
    if (Z_is_one(Z1))
    {
      // n3 = P2->x, n4 = P2->y
      call NN.Assign(n3, P2->x, NUMWORDS);
      call NN.Assign(n4, P2->y, NUMWORDS);
    }
    else
    {
      // n3 = P2->x * Z1^2
      call NN.ModSqrOpt(n0, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(n3, P2->x, n0, param.p, param.omega, NUMWORDS);
      // n4 = P2->y * Z1^3
      call NN.ModMultOpt(n0, n0, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(n4, P2->y, n0, param.p, param.omega, NUMWORDS);
    }
    
    // n5 = n1 - n3, n6 = n2 - n4
    call NN.ModSub(n5, n1, n3, param.p, NUMWORDS);
    call NN.ModSub(n6, n2, n4, param.p, NUMWORDS);
    
    if (call NN.Zero(n5, NUMWORDS))
      if (call NN.Zero(n6, NUMWORDS))
      {
        // P1 and P2 are same point
        c_dbl_projective(P0, Z0, P1, Z1);
	return;
      }
      else
      {
        // P1 is the inverse of P2
        call NN.AssignZero(Z0, NUMWORDS);
        return;
      }
    
    // 'n7' = n1 + n3, 'n8' = n2 + n4
    call NN.ModAdd(n1, n1, n3, param.p, NUMWORDS);
    call NN.ModAdd(n2, n2, n4, param.p, NUMWORDS);
    
    // Z0 = Z1 * Z2 * n5
    if (Z_is_one(Z1) && Z_is_one(Z2))
    {
      call NN.Assign(Z0, n5, NUMWORDS);
    }
    else
    {
      if (Z_is_one(Z1))
	call NN.Assign(n0, Z2, NUMWORDS);
      else if (Z_is_one(Z2))
	call NN.Assign(n0, Z1, NUMWORDS);
      else
	call NN.ModMultOpt(n0, Z1, Z2, param.p, param.omega, NUMWORDS);
	  
      call NN.ModMultOpt(Z0, n0, n5, param.p, param.omega, NUMWORDS);
    }
    
    // P0->x = n6^2 - n5^2 * 'n7'
    call NN.ModSqrOpt(n0, n6, param.p, param.omega, NUMWORDS);
    call NN.ModSqrOpt(n4, n5, param.p, param.omega, NUMWORDS);
    call NN.ModMultOpt(n3, n1, n4, param.p, param.omega, NUMWORDS);
    call NN.ModSub(P0->x, n0, n3, param.p, NUMWORDS);
	
    // 'n9' = n5^2 * 'n7' - 2 * P0->x
    call NN.LShift(n0, P0->x, 1, NUMWORDS);
    call NN.ModSmall(n0, param.p, NUMWORDS);
    call NN.ModSub(n0, n3, n0, param.p, NUMWORDS);
	
    // P0->y = (n6 * 'n9' - 'n8' * 'n5^3') / 2
    call NN.ModMultOpt(n0, n0, n6, param.p, param.omega, NUMWORDS);
    call NN.ModMultOpt(n5, n4, n5, param.p, param.omega, NUMWORDS);
    call NN.ModMultOpt(n1, n2, n5, param.p, param.omega, NUMWORDS);
    call NN.ModSub(n0, n0, n1, param.p, NUMWORDS);
	
    if ((n0[0] % 2) == 1)
      call NN.Add(n0, n0, param.p, NUMWORDS);
	
    call NN.RShift(P0->y, n0, 1, NUMWORDS);
   
  }

  // (P0,Z0) = 2*(P1,Z1)
  // P0 and P1 can be same pointer
  void c_dbl_projective(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1)
  {
    NN_DIGIT n0[NUMWORDS];
    NN_DIGIT n1[NUMWORDS];
    NN_DIGIT n2[NUMWORDS];
    NN_DIGIT n3[NUMWORDS];

    if (call NN.Zero(Z1, NUMWORDS))
    {
      call NN.AssignZero(Z0, NUMWORDS);
      return;
    }

    // n1
    if (Z_is_one(Z1))
    {
      // n1 = 3 * P1->x^2 + param.E.a
      call NN.ModSqrOpt(n0, P1->x, param.p, param.omega, NUMWORDS);
      call NN.LShift(n1, n0, 1, NUMWORDS);
      call NN.ModSmall(n1, param.p, NUMWORDS);
      call NN.ModAdd(n0, n0, n1, param.p, NUMWORDS);
      call NN.ModAdd(n1, n0, param.E.a, param.p, NUMWORDS);
    }
    else
    {
      if (param.E.a_minus3)
      {
        //for a = -3
	// n1 = 3 * (X1 + Z1^2) * (X1 - Z1^2) = 3 * X1^2 - 3 * Z1^4
	call NN.ModSqrOpt(n1, Z1, param.p, param.omega, NUMWORDS);
	call NN.ModAdd(n0, P1->x, n1, param.p, NUMWORDS);
	call NN.ModSub(n2, P1->x, n1, param.p, NUMWORDS);
	call NN.ModMultOpt(n1, n0, n2, param.p, param.omega, NUMWORDS);
	call NN.LShift(n0, n1, 1, NUMWORDS);
	call NN.ModSmall(n0, param.p, NUMWORDS);
	call NN.ModAdd(n1, n0, n1, param.p, NUMWORDS);
      }
      else if (param.E.a_zero)
      {
	// n1 = 3 * P1->x^2
	call NN.ModSqrOpt(n0, P1->x, param.p, param.omega, NUMWORDS);
	call NN.LShift(n1, n0, 1, NUMWORDS);
	call NN.ModSmall(n1, param.p, NUMWORDS);
	call NN.ModAdd(n1, n0, n1, param.p, NUMWORDS);
      }
      else
      {
	// n1 = 3 * P1->x^2 + param.E.a * Z1^4
	call NN.ModSqrOpt(n0, P1->x, param.p, param.omega, NUMWORDS);
	call NN.LShift(n1, n0, 1, NUMWORDS);
	call NN.ModSmall(n1, param.p, NUMWORDS);
	call NN.ModAdd(n0, n0, n1, param.p, NUMWORDS);
	call NN.ModSqrOpt(n1, Z1, param.p, param.omega, NUMWORDS);
	call NN.ModSqrOpt(n1, n1, param.p, param.omega, NUMWORDS);
	call NN.ModMultOpt(n1, n1, param.E.a, param.p, param.omega, NUMWORDS);
	call NN.ModAdd(n1, n1, n0, param.p, NUMWORDS);
      }
    }

    // Z0 = 2 * P1->y * Z1
    if (Z_is_one(Z1))
    {
      call NN.Assign(n0, P1->y, NUMWORDS);
    }
    else
    {
      call NN.ModMultOpt(n0, P1->y, Z1, param.p, param.omega, NUMWORDS);
    }
    call NN.LShift(Z0, n0, 1, NUMWORDS);
    call NN.ModSmall(Z0, param.p, NUMWORDS);

    // n2 = 4 * P1->x * P1->y^2
    call NN.ModSqrOpt(n3, P1->y, param.p, param.omega, NUMWORDS);
    call NN.ModMultOpt(n2, P1->x, n3, param.p, param.omega, NUMWORDS);
    call NN.LShift(n2, n2, 2, NUMWORDS);
    call NN.ModSmall(n2, param.p, NUMWORDS);

    // P0->x = n1^2 - 2 * n2
    call NN.LShift(n0, n2, 1, NUMWORDS);
    call NN.ModSmall(n0, param.p, NUMWORDS);
    call NN.ModSqrOpt(P0->x, n1, param.p, param.omega, NUMWORDS);
    call NN.ModSub(P0->x, P0->x, n0, param.p, NUMWORDS);

    // n3 = 8 * P1->y^4
    call NN.ModSqrOpt(n0, n3, param.p, param.omega, NUMWORDS);
    call NN.LShift(n3, n0, 3, NUMWORDS);
    call NN.ModSmall(n3, param.p, NUMWORDS);

    // P0->y = n1 * (n2 - P0->x) - n3
    call NN.ModSub(n0, n2, P0->x, param.p, NUMWORDS);
    call NN.ModMultOpt(n0, n1, n0, param.p, param.omega, NUMWORDS);
    call NN.ModSub(P0->y, n0, n3, param.p, NUMWORDS);

  }

  //m repeated point doublings (Algorithm 3.23 in "Guide to ECC")
  void c_m_dbl_projective(Point * P0, NN_DIGIT *Z0, uint8_t m){
    uint8_t i;
    NN_DIGIT W[NUMWORDS];
    NN_DIGIT A[NUMWORDS];
    NN_DIGIT B[NUMWORDS];
    NN_DIGIT t1[NUMWORDS];
    NN_DIGIT y2[NUMWORDS];
    
    if (call NN.Zero(Z0, NUMWORDS)){
      return;
    }

    //P0->y = 2*P0->y
    call NN.LShift(P0->y, P0->y, 1, NUMWORDS);
    call NN.ModSmall(P0->y, param.p, NUMWORDS);
    //W = Z^4
    call NN.ModSqrOpt(W, Z0, param.p, param.omega, NUMWORDS);
    call NN.ModSqrOpt(W, W, param.p, param.omega, NUMWORDS);
    
    for (i=0; i<m; i++){
      if (param.E.a_minus3){
	//A = 3(X^2-W)
	call NN.ModSqrOpt(A, P0->x, param.p, param.omega, NUMWORDS);
	call NN.ModSub(A, A, W, param.p, NUMWORDS);
	call NN.LShift(t1, A, 1, NUMWORDS);
	call NN.ModSmall(t1, param.p, NUMWORDS);
	call NN.ModAdd(A, A, t1, param.p, NUMWORDS);
      }else if (param.E.a_zero){
	//A = 3*X^2
	call NN.ModSqrOpt(t1, P0->x, param.p, param.omega, NUMWORDS);
	call NN.LShift(A, t1, 1, NUMWORDS);
	call NN.ModSmall(A, param.p, NUMWORDS);
	call NN.ModAdd(A, A, t1, param.p, NUMWORDS);
      }else{
	//A = 3*X^2 + a*W
	call NN.ModSqrOpt(t1, P0->x, param.p, param.omega, NUMWORDS);
	call NN.LShift(A, t1, 1, NUMWORDS);
	call NN.ModSmall(A, param.p, NUMWORDS);
	call NN.ModAdd(A, A, t1, param.p, NUMWORDS);
	call NN.ModMultOpt(t1, param.E.a, W, param.p, param.omega, NUMWORDS);
	call NN.ModAdd(A, A, t1, param.p, NUMWORDS);
      }
      //B = X*Y^2
      call NN.ModSqrOpt(y2, P0->y, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(B, P0->x, y2, param.p, param.omega, NUMWORDS);
      //X = A^2 - 2B
      call NN.ModSqrOpt(P0->x, A, param.p, param.omega, NUMWORDS);
      call NN.LShift(t1, B, 1, NUMWORDS);
      call NN.ModSmall(t1, param.p, NUMWORDS);
      call NN.ModSub(P0->x, P0->x, t1, param.p, NUMWORDS);
      //Z = Z*Y
      call NN.ModMultOpt(Z0, Z0, P0->y, param.p, param.omega, NUMWORDS);
      call NN.ModSqrOpt(y2, y2, param.p, param.omega, NUMWORDS);
      if (i < m-1){
	//W = W*Y^4
	call NN.ModMultOpt(W, W, y2, param.p, param.omega, NUMWORDS);
      }
      //Y = 2A(B-X)-Y^4
      call NN.LShift(A, A, 1, NUMWORDS);
      call NN.ModSmall(A, param.p, NUMWORDS);
      call NN.ModSub(B, B, P0->x, param.p, NUMWORDS);
      call NN.ModMultOpt(A, A, B, param.p, param.omega, NUMWORDS);
      call NN.ModSub(P0->y, A, y2, param.p, NUMWORDS);
    }
    if ((P0->y[0] % 2) == 1)
      call NN.Add(P0->y, P0->y, param.p, NUMWORDS);
    call NN.RShift(P0->y, P0->y, 1, NUMWORDS);
  }

  // precompute the array of the base point for sliding window method 
  void win_precompute(Point * baseP, Point * pointArray)
  {
    uint8_t i;
    
    call NN.Assign(pointArray[0].x, baseP->x, NUMWORDS);
    call NN.Assign(pointArray[0].y, baseP->y, NUMWORDS);
    
    for (i = 1; i < NUM_POINTS; i++)
    {
      c_add(&(pointArray[i]), &(pointArray[i-1]), baseP); 
    }
    
    for (i = 0; i < NUM_MASKS; i++)
      mask[i] = BASIC_MASK << (W_BITS*i);

  }
  
  //initialize parameters for ECC module
  command void ECC.init()
  {
    // get parameters
    call CurveParam.get_param(&param);
    
    //precompute array for base point
    win_precompute(&(param.G), pBaseArray);

  }
  
  command void ECC.get_order(NN_DIGIT * order)
  {
    call NN.Assign(order, param.r, NUMWORDS);
  }
  
  // curve routines
  // P0 = P1 + P2
  command void ECC.add(Point * P0, Point * P1, Point * P2)
  {
    c_add(P0, P1, P2);
  }

  // scalar point multiplication
  // P0 = n*P1
  // P0 and P1 can not be same pointer
  command void ECC.mul(Point * P0, Point * P1, NN_DIGIT * n)
  {
    int16_t i, tmp;
    NN_DIGIT Z0[NUMWORDS];
    NN_DIGIT Z1[NUMWORDS];

    // clear point
    p_clear(P0);
    
    //convert to Jprojective coordinate
    call NN.AssignZero(Z0, NUMWORDS);
    call NN.AssignZero(Z1, NUMWORDS);
    Z1[0] = 0x01;

    tmp = call NN.Bits(n, NUMWORDS);

    for (i = tmp-1; i >= 0; i--)
    {

      c_dbl_projective(P0, Z0, P0, Z0);

      if (b_testbit(n, i))
      {
        	
#ifdef ADD_MIX
	c_add_mix(P0, Z0, P0, Z0, P1);
#else
	c_add_projective(P0, Z0, P0, Z0, P1, Z1);
#endif
      }
    }   
    //convert back to affine coordinate
    if (!Z_is_one(Z0))
    {
      call NN.ModInv(Z1, Z0, param.p, NUMWORDS);
      call NN.ModMultOpt(Z0, Z1, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->x, P0->x, Z0, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(Z0, Z0, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->y, P0->y, Z0, param.p, param.omega, NUMWORDS);
    }

  }

  // precompute the array of base point for sliding window method
  command void ECC.win_precompute(Point * baseP, Point * pointArray)
  {
    uint8_t i;
    
    call NN.Assign(pointArray[0].x, baseP->x, NUMWORDS);
    call NN.Assign(pointArray[0].y, baseP->y, NUMWORDS);
    
    for (i = 1; i < NUM_POINTS; i++){
      c_add(&(pointArray[i]), &(pointArray[i-1]), baseP);
    }
    
    for (i = 0; i < NUM_MASKS; i++)
      mask[i] = BASIC_MASK << (W_BITS*i);


  }

  // scalar point multiplication
  // P0 = n*basepoint
  // pointArray is array of basepoint, pointArray[0] = basepoint, pointArray[1] = 2*basepoint ...
  void win_mul(Point * P0, NN_DIGIT * n, Point * pointArray)
  {
    
    int16_t i, tmp;
    int8_t j;
    NN_DIGIT windex;
    NN_DIGIT Z0[NUMWORDS];
    NN_DIGIT Z1[NUMWORDS];
#ifndef REPEAT_DOUBLE
    int8_t k;
#endif

    p_clear(P0);
    
    //convert to Jprojective coordinate
    call NN.AssignZero(Z0, NUMWORDS);
    call NN.AssignZero(Z1, NUMWORDS);
    Z1[0] = 0x01;	
    
    tmp = call NN.Digits(n, NUMWORDS);

    for (i = tmp - 1; i >= 0; i--){ 
      for (j = NN_DIGIT_BITS/W_BITS - 1; j >= 0; j--){

#ifndef REPEAT_DOUBLE
	for (k = 0; k < W_BITS; k++){
	  c_dbl_projective(P0, Z0, P0, Z0);
	}
#else
	c_m_dbl_projective(P0, Z0, W_BITS);
#endif

        windex = mask[j] & n[i];

        if (windex)
        {

          windex = windex >> (j*W_BITS);

#ifdef ADD_MIX 
	  c_add_mix(P0, Z0, P0, Z0, &(pointArray[windex-1]));
#else
	  c_add_projective(P0, Z0, P0, Z0, &(pointArray[windex-1]), Z1);
#endif
	}
      }	
    }

       
    //convert back to affine coordinate
    if (!Z_is_one(Z0))
    {
    
      call NN.ModInv(Z1, Z0, param.p, NUMWORDS);
      call NN.ModMultOpt(Z0, Z1, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->x, P0->x, Z0, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(Z0, Z0, Z1, param.p, param.omega, NUMWORDS);
      call NN.ModMultOpt(P0->y, P0->y, Z0, param.p, param.omega, NUMWORDS);
    }
    
  }
  
  /**
   * P0 = n * point, point is pointArray[0]
   * win_precompute must be called before win_mul
   */
  command void ECC.win_mul(Point * P0, NN_DIGIT * n, Point * pointArray)
  {
    win_mul(P0, n, pointArray);
  }
  
  /**
   * P0 = n * basepoint of curve
   * Don't need to call win_precompute before this func, cause init() has called win_precompute
   */
  command void ECC.win_mul_base(Point * P0, NN_DIGIT * n)
  {
    win_mul(P0, n, pBaseArray);
  }

  command Point * ECC.get_baseP(){
    return &(param.G);
  }

  command Params * ECC.get_param(){
    return &param;
  }

  command void ECC.add_proj(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2, NN_DIGIT * Z2){
    return c_add_projective(P0, Z0, P1, Z1, P2, Z2);
  }

  command void ECC.dbl_proj(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1){
    return c_dbl_projective(P0, Z0, P1, Z1);
  }
}
