<?php

/*
 * Following code will list all the users
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all users from users table
$result = mysql_query("SELECT *FROM users") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // users node
    $response["users"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $user = array();
        $user["pid"] = $row["pid"];
        $user["name"] = $row["name"];
        $user["position"] = $row["position"];
        $user["day"] = $row["day"];
        $user["month"] = $row["month"];
        $user["year_"] = $row["year_"];
        $user["start_time"] = $row["start_time"];
        $user["end_time"] = $row["end_time"];
        $user["need_cover"] = $row["need_cover"];



        // push single user into final response array
        array_push($response["users"], $user);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no users found
    $response["success"] = 0;
    $response["message"] = "No users found";

    // echo no users JSON
    echo json_encode($response);
}
?>
