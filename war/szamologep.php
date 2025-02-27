<?php
$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'GET') {
    $number1 = isset($_GET['number1']) ? $_GET['number1'] : 0;
    $number2 = isset($_GET['number2']) ? $_GET['number2'] : 0;
    $operator = isset($_GET['operator']) ? $_GET['operator'] : '+';
} elseif ($method == 'POST') {
    $number1 = isset($_POST['number1']) ? $_POST['number1'] : 0;
    $number2 = isset($_POST['number2']) ? $_POST['number2'] : 0;
    $operator = isset($_POST['operator']) ? $_POST['operator'] : '+';
} else {
    die("Nem támogatott metódus");
}

$number1 = floatval($number1);
$number2 = floatval($number2);
$operator = trim($operator); 

switch ($operator) {
    case "+":
        $result = $number1 + $number2;
        break;
    case "-":
        $result = $number1 - $number2;
        break;
    case "*":
        $result = $number1 * $number2;
        break;
    case "%":
        if ($number2 == 0) {
            $result = "Hiba: nullával való osztás!";
        } else {
            $result = $number1 % $number2;
        }
        break;
    default:
        $result = "Ismeretlen műveleti karakter! ($operator)";
        break;
}

echo $result;

?>
