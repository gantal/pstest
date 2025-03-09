<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=utf-8");

require_once 'DB.php';

$dsn = "mysqli://test:test@localhost/test_db";
$db = DB::connect($dsn);
if (PEAR::isError($db)) {
    die(json_encode(array("error" => "Kapcsolódási hiba: " . $db->getMessage())));
}

$input = file_get_contents("php://input");
$data = json_decode($input, true);

if (!$data) {
    die(json_encode(array("error" => "Hibás JSON formátum.")));
}

$idsToDelete = [];
foreach ($data as $key => $value) {
    $idsToDelete[] = (int) $value;
}

if (empty($idsToDelete)) {
    die(json_encode(array("error" => "Nincs kijelölt elem törlésre.")));
}

$placeholders = implode(",", array_fill(0, count($idsToDelete), "?"));
$sql = "DELETE FROM data WHERE id IN ($placeholders)";
$stmt = $db->prepare($sql);

if (PEAR::isError($stmt)) {
    die(json_encode(array("error" => "SQL előkészítési hiba: " . $stmt->getMessage())));
}

$result = $db->execute($stmt, $idsToDelete);

if (PEAR::isError($result)) {
    die(json_encode(array("error" => "Törlési hiba: " . $result->getMessage())));
}

echo json_encode(array("success" => true, "deleted" => count($idsToDelete)));

$db->disconnect();
?>
