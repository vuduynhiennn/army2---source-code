<?php
include '../res/connect.php';
if (!$login || !$connect)
    exit(0);
    
if ($_GET['id'] > 0 && $armymem['clan'] > 0 && $clanmem['rights'] > 0) {
    $RuongItem = json_decode($clan['Item'], true);
    foreach ($RuongItem as $key => $Item) {
        if ($Item['id'] == $_GET['id']) {
            array_splice($RuongItem, $key, 1);
            $sql = "UPDATE `clan` SET `item` = '". addslashes(json_encode($RuongItem)) ."' WHERE `id` = '". $clan['id'] ."'; ";
            $conn->query($sql);
            header('Location: ../?c=info&f=item');
            break;
        }
    }
}

?>