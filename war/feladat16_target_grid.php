<?php
require_once "db_config.php";

header('Content-Type: application/json');

$db = connectDB("target_db");
if (PEAR::isError($db)) {
    die(json_encode(["error" => "Kapcsolódási hiba: " . $db->getMessage()]));
}

if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    $query = "SELECT * FROM orders ORDER BY id ASC";
    $result = $db->query($query);

    if (PEAR::isError($result)) {
        die(json_encode(["error" => "Lekérdezési hiba: " . $result->getMessage()]));
    }

    $data = [];
    while ($row = $result->fetchRow(DB_FETCHMODE_ASSOC)) {
        $row['id'] = (int)$row['id'];
        $row['quantity'] = (int)$row['quantity'];
        $row['price'] = (double)$row['price'];
        $data[] = $row;
    }

    echo json_encode($data, JSON_UNESCAPED_UNICODE);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $inputJSON = file_get_contents("php://input");
    error_log("Kapott JSON: " . $inputJSON);

    $input = json_decode($inputJSON, true);
    if (!$input) {
        die(json_encode(["error" => "Hibás JSON formátum"]));
    }

    $product = isset($input['product']) ? $db->quoteSmart($input['product']) : null;
    $quantity = isset($input['quantity']) ? (int)$input['quantity'] : null;
    $price = isset($input['price']) ? (float)$input['price'] : null;
    $orderDate = isset($input['orderDate']) ? $db->quoteSmart($input['orderDate']) : null;

    if (!$product || !$quantity || !$price || !$orderDate) {
        die(json_encode(["error" => "Hiányzó adatok!"]));
    }

    $query = "INSERT INTO orders (product, quantity, price, orderDate) VALUES ($product, $quantity, $price, $orderDate)";
    error_log("Futtatott SQL: " . $query);

    $result = $db->query($query);

    if (PEAR::isError($result)) {
        die(json_encode(["error" => "Hiba az adatbázis beszúrás során: " . $result->getMessage()]));
    }

    echo json_encode(["success" => true, "id" => $db->getOne("SELECT LAST_INSERT_ID()")]);
    exit;
}

if ($_SERVER['REQUEST_METHOD'] === 'DELETE') {
    if (!isset($_GET['id'])) {
        die(json_encode(["error" => "Hiányzó ID paraméter!"]));
    }

    $id = (int)$_GET['id'];
    $query = "DELETE FROM orders WHERE id = $id";
    $result = $db->query($query);

    if (PEAR::isError($result)) {
        die(json_encode(["error" => "Hiba a törlés során: " . $result->getMessage()]));
    }

    echo json_encode(["success" => true]);
    exit;
}

http_response_code(405);
echo json_encode(["error" => "Érvénytelen kérés"]);
exit;
?>
