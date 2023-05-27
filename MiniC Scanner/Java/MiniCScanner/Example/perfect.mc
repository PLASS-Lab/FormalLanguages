/*
    A perfect number is an integer which is equal to the sum of all
    its divisors including 1 but excluding the number itself.
    연습문제 2.18 참조
*/

const int max = 500;

void main()
{ 
  int i, j, k;
  int rem, sum;		// rem: remainder

  i = 2;
  while (i <= max) {
     sum = 0;
     k = i / 2;
     j = 1;
     while (j <= k) {
          rem = i % j;
          if (rem == 0) sum += j;
          ++j;
     }
     if (i == sum) write(i);
     ++i;
  }
}
