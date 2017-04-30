<?php

/*
 * Following code will update a user information
 * A user is identified by user id (pid)
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['db_name']) && isset($_POST['db_field_ref']) && isset($_POST['val_ref']) && isset($_POST['db_field_rep']) && isset($_POST['val_rep']) ) {
    
    $db_name = $_POST['db_name'];
    $db_field_ref = $_POST['db_field_ref'];
    $val_ref = $_POST['val_ref'];
    $db_field_rep = $_POST['db_field_rep'];
    $val_rep = $_POST['val_rep'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched pid
    $result = mysql_query("UPDATE $db_name SET $db_field_rep = '$val_rep' WHERE $db_field_ref = '$val_ref' ");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "User successfully updated.";
        
        // echoing JSON response
        echo json_encode($response);
    } else {
        
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
