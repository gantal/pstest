<?php
$array = [3, 21, 2, 48, 17, 54, 43, 65, 8];
$json_array = json_encode($array);
print_r("JSON tömb: " . $json_array . "<br/><br/>");
$decoded_array = json_decode($json_array, true);
$min = min($decoded_array);
$max = max($decoded_array);
echo "Legkisebb elem érték: " . $min . "<br/>";
echo "Legnagyobb elem érték: " . $max . "<br/>";

$json_result = json_encode([
    "min" => $min,
    "max" => $max
], JSON_PRETTY_PRINT);
print_r("JSON eredmények: <br/>" . str_replace("\n", "<br/>", $json_result));
echo "<br/>";
echo "----------------------------------- <br/>";
$numbers = [
    [10, 20, 30],
    [40, 50, 60],
    [70, 80, 90]
];

echo "<pre>";
foreach ($numbers as $row) {
    echo implode("\t", $row) . "\n";
}
echo "</pre>";

$search = 50;
$found = false;
$position = null;

foreach ($numbers as $rowIndex => $row) {
    $colIndex = array_search($search, $row);
    if ($colIndex !== false) {
        $found = true;
        $position = [
            "row" => $rowIndex,
            "column" => $colIndex
        ];
        break;
    }
}

if ($found) {
    print_r("A keresett szám az: " . $search . "<br/>");
    print_r("Pozíciója: Sor " . $position["row"] . ", Oszlop " . $position["column"] . "<br/>");
} else {
    print_r("A szám nem található.");
}
echo "----------------------------------- <br/>";

function factorial($n)
{
    if ($n <= 1) {
        return 1;
    }
    return $n * factorial($n - 1);
}

$number = 5;
$result = factorial($number);
print_r("A(z) " . $number . " faktoriálisa: " . $result);
echo "----------------------------------- <br/>";

function euclidean_gcd($a, $b)
{
    $steps = [];

    while ($b != 0) {
        $remainder = $a % $b;
        $steps[] = "a: $a, b: $b, maradék: $remainder";
        $a = $b;
        $b = $remainder;
    }

    print_r("Euklédeszi algoritmus lépései:<br/>");
    foreach ($steps as $step) {
        print_r($step . "<br/>");
    }
    print_r("<br/>A legnagyobb közös osztó (LNKO) : $a<br/>");
}

$number1 = 48;
$number2 = 18;

euclidean_gcd($number1, $number2);

?>
