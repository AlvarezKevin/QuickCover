<?php

/*
 * Following code will create a new user row
 * All user details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['db_name']) && isset($_POST['db_var'])) {
    
    $db_name = $_POST['db_name'];
    $db_var = $_POST['db_var'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("SELECT *FROM db_name = $db_name WHERE db_var = $db_var");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

        // looping through all results
        // users node
            $response["db_name"] = array();
    
            while ($row = mysql_fetch_array($result)) {
                // temp user array
                $var = array();
                $var["db_var"] = $row["db_var"];
                // push single user into final response array
                array_push($response["db_name"], $var);
            }
            // success
            $response["success"] = 1;

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no users found
            $response["success"] = 0;
            $response["message"] = "No value found";

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