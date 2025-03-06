<?php
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=utf-8");

require_once 'DB.php';

$dsn = "mysqli://test:test@localhost/test_db";
$db = DB::connect($dsn);
if (PEAR::isError($db)) {
    die(json_encode(array("error" => "Kapcsolódási hiba: " . $db->getMessage())));
}

$limit = isset($_GET['limit']) ? (int)$_GET['limit'] : 2; 
$offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;
$sortField = isset($_GET['sortField']) ? $_GET['sortField'] : 'id';
$sortDir = isset($_GET['sortDir']) && strtoupper($_GET['sortDir']) == 'DESC' ? 'DESC' : 'ASC';

$totalQuery = "SELECT COUNT(*) as total FROM data";
$totalResult = $db->query($totalQuery);
$totalRow = $totalResult->fetchRow(DB_FETCHMODE_ASSOC);
$total = $totalRow['total'];

$sql = "SELECT * FROM data ORDER BY $sortField $sortDir LIMIT $limit OFFSET $offset";
$result = $db->query($sql);

$data = array();
while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
    $data[] = array(
        "id" => (int)$row['id'],
        "product" => $row['product'],
        "quantity" => (int)$row['quantity'],
        "price" => (float)$row['price'],
        "orderDate" => $row['orderDate']
    );
}

echo json_encode(array(
    "success" => true,
    "total" => $total,
    "data" => $data
));

$db->disconnect();
?>
