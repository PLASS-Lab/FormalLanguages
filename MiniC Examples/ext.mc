/*
      ext.mc: test the external variable
*/

int x;

void main()
{ 
  func();
  write(x);
}

void func()
{
  x = 100;
}
