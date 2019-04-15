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
 * Interface for ECC operation
 *
 * Author: An Liu
 * Date: 09/15/2005
 */

includes NN;
includes ECC;

interface ECC {

  // init the parameters and base point array for sliding window method
  // the first function to call
  command void init();
    
  // provide order of curve for the modules which need to know
  command void get_order(NN_DIGIT * order);
    
  // Point addition, P0 = P1 + P2
  command void add(Point * P0, Point * P1, Point * P2);

  // projective point addition, P0 = P1 + P2
  command void add_proj(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1, Point * P2, NN_DIGIT * Z2);

  // projective point doubleing, P0 = 2*P1
  command void dbl_proj(Point * P0, NN_DIGIT *Z0, Point * P1, NN_DIGIT * Z1);
    
  // Scalar point multiplication P0 = n * P1
  command void mul(Point * P0, Point * P1, NN_DIGIT * n);
    
  // precompute the points for sliding window method
  command void win_precompute(Point * baseP, Point * pointArray);
    
  // scalr point multiplication using slide window method
  // P0 = n * Point, this Point may not be the base point of curve
  // pointArray is constructed by call win_precompute(Point, pointArray)
  command void win_mul(Point * P0, NN_DIGIT * n, Point * pointArray);
    
  // scalr point multiplication using slide window method, P0 = n * basePoint of curve
  command void win_mul_base(Point * P0, NN_DIGIT * n);

  //get base point
  command Point * get_baseP();

  //get parameters
  command Params * get_param();

}
