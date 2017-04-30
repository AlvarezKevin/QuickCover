<?php

/*
 * Following code will get single user details
 * A user is identified by user id (pid)
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["pid"])) {
    $pid = $_GET['pid'];

    // get a user from users table
    $result = mysql_query("SELECT *FROM users WHERE pid = $pid");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);

            $user = array();
            $user["pid"] = $result["pid"];
            $user["name"] = $result["name"];
            $user["position"] = $result["price"];
            $user["day"] = $result["day"];
            $user["month"] = $result["month"];
            $user["year_"] = $result["year_"];
            $user["start_time"] = $result["start_time"];
            $user["end_time"] = $result["end_time"];
            $user["need_cover"] = $result["need_cover"];
           
            // success
            $response["success"] = 1;

            // user node
            $response["user"] = array();

            array_push($response["user"], $user);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no user found
            $response["success"] = 0;
            $response["message"] = "No user found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no user found
        $response["success"] = 0;
        $response["message"] = "No user found";

        // echo no users JSON
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>