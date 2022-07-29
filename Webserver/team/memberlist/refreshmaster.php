<?php
include('../res/connect.php');
if (!$login || !$connect)
    exit(0);
    
if ($armymem['clan'] > 0 && $armymem['id'] == $clan['master']) {

    $idS =  $conn->real_escape_string($_GET['id']);

    if ($idS > 0) {
        $sql = "SELECT `user`,`user_name` FROM `clanmem` WHERE (`user` = '". $idS ."' AND `clan` = '". $armymem['clan'] ."') LIMIT 1; ";
        $result = $conn->query($sql);
        
        if ($result->num_rows > 0 && $clan['master'] != $idS) {
        
            $cmem = $result->fetch_assoc();
            
            $sql = "UPDATE `clan` SET `master` = ". $cmem['user'] .", `masterName` = '". $cmem['user_name'] ."' WHERE `id` = '". $clan['id'] ."'; ";
            $conn->query($sql);
            
            $sql = "UPDATE `clanmem` SET `rights` = 0 WHERE `user` = '". $user['user_id'] ."'; ";
            $conn->query($sql);
            
            $sql = "UPDATE `clanmem` SET `rights` = 2 WHERE `user` = '". $cmem['user'] ."'; ";
            $conn->query($sql);
            
            header('Location: ../?c=list');
        }
    }

}

echo 'Error';
?>