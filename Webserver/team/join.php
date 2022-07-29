<?php

if (!$login || !$connect)
    exit(0);
    
if ($armymem['request_clan'] == 0 && $armymem['clan'] == 0) {
        
    $idClan = $conn->real_escape_string($_GET['id']);
     
    $sql = "SELECT * FROM `clan` WHERE `id` = '". $idClan ."' LIMIT 1; ";
    $result = $conn->query($sql);
    if ($result->num_rows > 0) {
    
        $clan = $result->fetch_assoc();
        
        include 'res/head.php';
        
        $nv = json_decode($armymem['NV'.$armymem['NVused']],true);
        
        if ($clan['mem'] > $clan['memMax'])
        
            echo "\n" . '<br /><font color="red">* Lỗi: Đội này đã đủ số thành viên cho phép.</font><br />';
        
        else if ($nv['lever'] < 2)
        
            echo "\n" . '<br /><font color="red">* Lỗi: Nhân vật bạn đang chọn phải hơn cấp 2.</font><br />';
        
        else {
        
            $sql = "UPDATE `armymem` SET `clan` = 0, `request_clan` = '". $clan['id'] ."' WHERE `id` = '". $armymem['id'] ."'; ";
            $conn->query($sql);
            header('Location: ?c=list');
        
        }
        
        include 'res/end.php';
        
    } else header('Location: ?c=list');
    
}
?>