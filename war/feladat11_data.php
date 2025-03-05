<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=utf-8");

require_once 'DB.php';

$dsn = "mysqli://test:test@localhost/test_db";

$db =& DB::connect($dsn);
if (PEAR::isError($db)) {
    die(json_encode(array("error" => "Kapcsolódási hiba: " . $db->getMessage())));
}

$sql = "SELECT id, product, quantity, price, orderDate FROM data";
$result = $db->getAll($sql, null, DB_FETCHMODE_ASSOC);

if (!PEAR::isError($result)) {
    echo json_encode(array("data" => $result));
} else {
    echo json_encode(array("error" => "Adatlekérési hiba: " . $result->getMessage()));
}

$db->disconnect();
?>
