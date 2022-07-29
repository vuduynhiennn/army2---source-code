<?php

//import func

include ('func.php');

//config 

$ip_sv = "localhost";
$dbname_sv = "dbarmy2";
$user_sv = "root";
$pass_sv = "";

//GMT +7

date_default_timezone_set('Asia/Ho_Chi_Minh');

// Create connection

$conn = new mysqli($ip_sv, $user_sv, $pass_sv, $dbname_sv);
    
// Check connection
    
if ($conn->connect_error) {
    
    die("Connection failed: " . $conn->connect_error);
    exit(0);
        
}
$conn->query("SET NAMES utf8");
$login = false;
$connect = true;
session_name('kitakeyos');
session_start();

if ($_GET['c'] == 'logout') {
    unset($_SESSION['uid']);
    header('Location: ?c=login');
}
$login = isset($_SESSION['uid']);

if ($login) {
    
    $sql = "SELECT * FROM `user` WHERE `user_id` = ".$_SESSION['uid']." LIMIT 1;";
    $user = $conn->query($sql)->fetch_assoc();
    
    $sql = "SELECT * FROM `armymem` WHERE `id` = ".$_SESSION['uid']." LIMIT 1;";
    $armymem = $conn->query($sql)->fetch_assoc();
    
    if ($armymem && $armymem['clan'] > 0) {
    
        $sql = "SELECT * FROM `clan` WHERE `id` = ". $armymem['clan'] ." LIMIT 1;";
        $clan = $conn->query($sql)->fetch_assoc();
        
        $sql = "SELECT * FROM `clanmem` WHERE `user` = ". $armymem['id'] ." LIMIT 1;";
        $clanmem = $conn->query($sql)->fetch_assoc();
    
    
    }
    
}
 ?>