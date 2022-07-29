<?php
include ('../res/connect.php');
if (!$login || !$connect)
    exit(0);
    
if ($armymem['clan'] > 0 && $armymem['id'] == $clan['master']) {
     
    $Ids = $conn->real_escape_string($_GET['id']);
     
    $sql = "SELECT `id` FROM `armymem` WHERE (`id` = '". $Ids ."' AND `clan` = '". $clan['id'] ."') LIMIT 1; ";
    $result = $conn->query($sql);
    $amem = $result->fetch_assoc();
    
    $sql = "SELECT `id` FROM `armymem` WHERE (`id` = '". $Ids ."' AND `clan` = '". $clan['id'] ."') LIMIT 1; ";
    $num_rights = $conn->query("SELECT `id` FROM `clanmem` WHERE (`rights` > 0 AND `user` = '". $clan['master'] ."' AND `clan` = '". $clan['id'] ."'); ")->num_rows;
    
    if ($result->num_rows > 0 && $clan['master'] != $amem['id']) {
    
        $sql = "SELECT * FROM `clanmem` WHERE (`user` = '". $Ids ."' AND `clan` = '". $clan['id'] ."') LIMIT 1; ";
        $result = $conn->query($sql);
        $member = $result->fetch_assoc();
        
        if ($member['rights'] > 0) {
    
            $sql = "UPDATE `clanmem` SET `rights` = 0 WHERE `user` = '". $amem['id'] ."'; ";
            $conn->query($sql);
            
        } else if ($num_rights < 4) {
        
            $sql = "UPDATE `clanmem` SET `rights` = 1 WHERE `user` = '". $amem['id'] ."'; ";
            $conn->query($sql);
            
        }
        
        header('Location: ../?c=info&f=memberlist');
    }
}
?>