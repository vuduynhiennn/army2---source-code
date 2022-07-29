<?php
include ('../res/connect.php');
if (!$login || !$connect)
    exit(0);
    
if ($armymem['clan'] > 0 && ($armymem['id'] == $clan['master'] || $clanmem['rights'] > 0)) {
     
    $Ids = $conn->real_escape_string($_GET['id']);
     
    $sql = "SELECT `id` FROM `armymem` WHERE (`id` = '". $Ids ."' AND `clan` = '". $clan['id'] ."') LIMIT 1; ";
    $result = $conn->query($sql);
    $amem = $result->fetch_assoc();
    
    $sql = "SELECT * FROM `clanmem` WHERE `user` = '". $Ids . "' LIMIT 1; ";
    $result = $conn->query($sql);
    $cmem = $result->fetch_assoc();
    
    if ($result->num_rows > 0 && ($clan['master'] != $amem['id'] && ($clan['master'] == $armymem['id'] || ($clanmem['rights'] > 0 && $cmem['rights'] == 0)))) {
    
        $sql = "UPDATE `armymem` SET `clan` = 0, `request_clan` = 0 WHERE `id` = '". $amem['id'] ."'; ";
        $conn->query($sql);
        
        $sql = "DELETE FROM `clanmem` WHERE `user` = '". $amem['id'] ."'; ";
        $conn->query($sql);
            
        $sql = "UPDATE `clan` SET `mem` = `mem` -1 WHERE (`id` = '". $clan['id'] ."' AND `mem` > 0); ";
        $conn->query($sql);
        
        header('Location: ../?c=info&f=memberlist');
    }
}
?>