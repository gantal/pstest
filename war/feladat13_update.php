<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=utf-8");

require_once 'DB.php';

$dsn = "mysqli://test:test@localhost/test_db";
$db =& DB::connect($dsn);
if (PEAR::isError($db)) {
    die(json_encode(array("error" => "Kapcsolódási hiba: " . $db->getMessage())));
}

$data = json_decode(file_get_contents("php://input"), true);
if (!$data || !isset($data['id'])) {
    die(json_encode(array("error" => "Hibás bemenet!")));
}

$sql = "UPDATE data SET product = ?, quantity = ?, price = ?, orderDate = ? WHERE id = ?";
$params = array($data['product'], $data['quantity'], $data['price'], $data['orderDate'], $data['id']);
$result = $db->query($sql, $params);

if (PEAR::isError($result)) {
    echo json_encode(array("error" => "Módosítási hiba: " . $result->getMessage()));
} else {
    echo json_encode(array("success" => true));
}

$db->disconnect();
?>
