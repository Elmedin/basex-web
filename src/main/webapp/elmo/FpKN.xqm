module namespace fpkn = "FpKN.xqm";

declare function fpkn:minPrice($p as xs:decimal?, $d as xs:decimal?)
as xs:decimal?
{
    let $disc := ($p * $d) div 100
    return ($p - $disc)
};


        
 