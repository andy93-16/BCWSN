CC= gcc
CFLAGS= -g -Wall -DTEST
LDFLAGS= $(SYSLDFLAGS) $(MYLDFLAGS)
LIBS=
SYSCFLAGS=
SYSLDFLAGS=
SYSLIBS=

OBJS= tinyhash.o test.o

all: test

test: $(OBJS)
	$(CC) -o $@ $(LDFLAGS) $^ $(LIBS)

tinyhash.o: tinyhash.c tinyhash.h
test.o: test.c tinyhash.h

clean:
	rm -f $(OBJS) test
