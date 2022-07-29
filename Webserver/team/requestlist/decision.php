<?php
include ('../res/connect.php');
if (!$login || !$connect)
    exit(0);
$Ids = $conn->real_escape_string($_GET['id']);
$type = $_GET['type'];
if ($armymem['clan'] > 0 && $Ids > 0 && $clanmem['rights'] > 0) {
    switch($type) {
        case 'duyet':
            if ($clan['mem'] < $clan['memMax']) {
                $aresult = $conn->query("SELECT * FROM `armymem` WHERE (`id` = '". $Ids ."' AND `clan` = 0 AND `request_clan` = '". $clan['id'] ."') LIMIT 1; ");
                if ($aresult->num_rows > 0) {   
                    $amem = $aresult->fetch_assoc();
                    $uresult = $conn->query("SELECT `user` FROM `user` WHERE `user_id` = '". $amem['id'] ."' LIMIT 1; ");
                    $umem = $uresult->fetch_assoc();
                
                    $sql = "INSERT INTO `clanmem` (`clan`, `user`, `user_name`) VALUES ('". $clan['id'] ."', '". $amem['id'] ."', '". $umem['user'] ."')";
                    $conn->query($sql);
                
                    $sql = "UPDATE `armymem` SET `clan` = '". $clan['id'] . "', `request_clan` = '". $clan['id'] ."' WHERE `id` = '". $amem['id'] ."'; ";
                    $conn->query($sql);
            
                    $number_mem = $conn->query("SELECT `id` FROM `armymem` WHERE `clan` = '". $clan['id'] ."'; ")->num_rows;
            
                    $sql = "UPDATE `clan` SET `mem` = '". $number_mem . "' WHERE `id` = '". $clan['id'] ."'; ";
                    $conn->query($sql);
                    header('Location: ../?c=info&f=requestlist');
                }
            }
            break;
        case 'tc':
            $aresult = $conn->query("SELECT * FROM `armymem` WHERE (`id` = '". $Ids ."' AND `clan` = 0 AND `request_clan` = '". $clan['id'] ."') LIMIT 1; ");
        
            if ($aresult->num_rows > 0) {
                $amem = $aresult->fetch_assoc();
                $uresult = $conn->query("SELECT `user` FROM `user` WHERE `user_id` = '". $amem['id'] ."' LIMIT 1; ");
                $umem = $uresult->fetch_assoc();
            
                $sql = "UPDATE `armymem` SET `clan` = 0, `request_clan` = 0 WHERE `id` = '". $amem['id'] ."'; ";
                $conn->query($sql);
                header('Location: ../?c=info&f=requestlist');
            }
            break;    
    }
}

?>