program GCD;
/* A program to compute the greatest common of two numbers,
   i.e., the biggest numbers by which the two original
   numbers can be divide without a remainder. */

const v1 = 1071; v2 = 462;

var res: integer;

function GCD (m: integer; n: integer): integer;
begin
        GCD := GCD(n, m mod n)
end; { GCD }

begin
    res := GCD(v1,v2);
    write('GCD(', v1, ',', v2, ') = ', res, eol);
end.

