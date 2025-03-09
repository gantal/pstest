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

$filterQuery = "";
if (isset($_GET['filter']) && !empty($_GET['filter'])) {
    $filters = explode(",", $_GET['filter']);
    $filterConditions = array();

    foreach ($filters as $filter) {
        list($field, $value) = explode(":", $filter, 2);
        $field = trim($field);
        $value = trim($value);

        if (!empty($field) && !empty($value)) {
            if (strpos($value, "lt=") !== false || strpos($value, "gt=") !== false || strpos($value, "eq=") !== false) {
                $conditions = explode("_", $value);
                foreach ($conditions as $condition) {
                    if (strpos($condition, "lt=") !== false) {
                        $number = str_replace("lt=", "", $condition);
                        $filterConditions[] = "$field < $number";
                    } elseif (strpos($condition, "gt=") !== false) {
                        $number = str_replace("gt=", "", $condition);
                        $filterConditions[] = "$field > $number";
                    } elseif (strpos($condition, "eq=") !== false) {
                        $number = str_replace("eq=", "", $condition);
                        $filterConditions[] = "$field = $number";
                    }
                }
            } elseif ($field == "orderDate") {
                $value = date('Y-m-d', strtotime($value));
                $filterConditions[] = "$field = '$value'";
            } elseif (is_numeric($value)) {
                $filterConditions[] = "$field = $value";
            } else {
                $filterConditions[] = "$field LIKE '%$value%'";
            }
        }
    }

    if (!empty($filterConditions)) {
        $filterQuery = " WHERE " . implode(" AND ", $filterConditions);
    }
}

$totalQuery = "SELECT COUNT(*) as total FROM data $filterQuery";
$totalResult = $db->query($totalQuery);
$totalRow = $totalResult->fetchRow(DB_FETCHMODE_ASSOC);
$total = $totalRow['total'];

$sql = "SELECT * FROM data $filterQuery ORDER BY $sortField $sortDir LIMIT $limit OFFSET $offset";
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
