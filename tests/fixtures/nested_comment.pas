/* Det er lov å ha kommentarer utenpå hverandre,
   om de er av ulik type,
   for eksempel slik: {indre kommentar}.

   Men det er ikke lov når de er av samme type,
   for eksempel slik: /*indre kommentar*/
   som _ikke_ er lov.
*/

program NesteteKommentarer;
begin
   write('Dette programmet har kommentarer inni andre kommentarer.', eol);
end.
