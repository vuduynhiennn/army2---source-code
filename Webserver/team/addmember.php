<?php

if (!$login || !$connect)
    exit(0);
    
if ($armymem['clan'] > 0 && $armymem['id'] == $clan['master']) {

    include 'res/head.php';

        if ($clan['luong'] >= 100 && $clan['memMax'] < 100) {
        
            $sql = "UPDATE `clan` SET `memMax` = `memMax` + 5, `luong` = `luong` - 100 WHERE `id` = '". $armymem['clan'] ."'; ";
            $conn->query($sql);
              
        }
        header('Location: ?c=info');
} else header('Location: ?c=list');

?>