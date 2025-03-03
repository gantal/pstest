<?php
header("Access-Control-Allow-Origin: *");  
header('Content-Type: application/json; charset=utf-8');

$data = array();

$threeMonthsAgo = strtotime("-3 months");
$now = time();

$numRecords = 10;

for ($i = 1; $i <= $numRecords; $i++) {
    $randomTimestamp = rand($threeMonthsAgo, $now);
    $randomDate = date("Y-m-d", $randomTimestamp);

    $data[] = array(
        "id"        => $i,
        "product"   => "Termék " . $i,
        "quantity"  => rand(1, 20),
        "price"     => rand(10000, 99999),
        "orderDate" => $randomDate
    );
}

echo json_encode($data);
?>