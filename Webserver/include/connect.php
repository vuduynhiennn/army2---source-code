<?php 
 $cfg = $config['mysql'];
 $game = new mysqli($cfg['server'].':'.$cfg['port'], $cfg['user'], $cfg['password'], $cfg['database']);
  if ($game->connect_error) {
    
    die("Connection failed: " . $game->connect_error);
    exit(0);
        
  }
  $game->query("SET NAMES utf8");
  $user = array();
  $armymem = array();
  $user_id = 1;
  $createdCharacter = false;
  if (isset($_SESSION['user_id'])) {
    $user_id = $_SESSION['user_id'];
    $sql = "SELECT * FROM `user` WHERE `user_id` = ".$user_id." LIMIT 1;";
    $user = $game->query($sql)->fetch_assoc();
    
    $sql = "SELECT * FROM `armymem` WHERE `id` = ".$user_id." LIMIT 1;";
    $armymem = $game->query($sql)->fetch_assoc();
    if ($armymem != null) {
      $createdCharacter = true;
    }
  }
?>