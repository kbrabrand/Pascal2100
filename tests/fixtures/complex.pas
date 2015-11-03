program A;
var X: Integer;

procedure A (V: Integer);
type A = Integer;

function F (V: A): A;
begin
    X := 2*V;
    F := X
end; { F }

begin
    X := X + F(V)
end; { A }

begin
    X := 1;
    A(10);
    write('X = ', X, eol)
end.
