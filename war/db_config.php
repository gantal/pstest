<?php
require_once 'DB.php';

$host = "localhost";
$user = "test";
$pass = "test";

function connectDB($database) {
    global $user, $pass;
    $dsn = "mysqli://$user:$pass@localhost/$database";
    
    $db = DB::connect($dsn);
    
    if (PEAR::isError($db)) {
        die(json_encode(["error" => "Kapcsolódási hiba: " . $db->getMessage()]));
    }
    
    return $db;
}

?>
