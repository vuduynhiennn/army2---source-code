<?php

if (!$login || !$connect)
    exit(0);
    
if ($armymem['request_clan'] > 0) {
        
    $sql = "SELECT * FROM `clan` WHERE `id` = '". $armymem['request_clan'] ."' LIMIT 1; ";
    $result = $conn->query($sql);
    $clan = $result->fetch_assoc();
    
    if ($clan['master'] != $armymem['id'] && ($armymem['clan'] == 0 || ($armymem['xu'] >= 1000 && $armymem['clanpoint'] >= 100))) {
    
        $sql = "UPDATE `armymem` SET `clan` = 0, `request_clan` = 0 WHERE `id` = '". $armymem['id'] ."'; ";
        $conn->query($sql);
        
        if ($armymem['clan'] > 0) {
        
            $sql = "UPDATE `armymem` SET `xu` = `xu` - 1000, `clanpoint` = `clanpoint` - 100 WHERE `id` = '". $armymem['id'] ."'; ";
            $conn->query($sql); 
            
            $sql = "DELETE FROM `clanmem` WHERE `user` = '". $armymem['id'] ."'; ";
            $conn->query($sql);
            
            $sql = "UPDATE `clan` SET `mem` = `mem` -1 WHERE (`id` = '". $clan['id'] ."' AND `mem` > 0); ";
            $conn->query($sql); 
            
        }
            
        header('Location: ?c=list');
        
    } else if ($armymem['clan'] > 0 && $clan['master'] != $armymem['id']) {
        include('./res/head.php');
        
        echo '
            <p style="color:red">Không đủ xu hoặc điểm biệt đội <a href="?c=list">quay lại</a></p>
        ';
        
        include('./res/end.php');
    }
   
}
?>