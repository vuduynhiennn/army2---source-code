<?php
$get = $_GET;
if(!empty($get)){
  include '../include/config.php';
  include '../include/connect.php';
  $status		      = (int) addslashes($get['status']);
  $pricesvalue    = addslashes($get['pricesvalue']);
  $value_receive  = addslashes($get['value_receive']);
  $card_code	    = addslashes($get['card_code']);
  $card_seri	    = addslashes($get['card_seri']);
  $requestid 	    = addslashes($get['requestid']);
  $amount         = $pricesvalue;
  if($status == 200 || $status == 201) {
    //the dung
    $sql    = "SELECT * FROM `topup` WHERE `requestid`='" . $requestid . "'";
    $row    = $game->query($sql)->fetch_assoc();
    if($row['status'] != 0){
      header('location: /home');
    }
    if($status == 201){
      $amount = $pricesvalue < $value_receive ? $pricesvalue : $value_receive;
    }
    if ($row['type'] == 'napxu') {
      $game->query("UPDATE `armymem` SET `xu` = `xu` + ".((int) $amount * 10000)." WHERE `id` = ".$row['user_id'].";");
      execute(json_encode($msg));
    } else if ($row['type'] == 'napluong'){
      $game->query("UPDATE `armymem` SET `luong` = `luong` + ".$amount." WHERE `id` = ".$row['user_id'].";");
    } else {
      $game->query("UPDATE `armymem` SET `money` = `money` + ".$amount." WHERE `id` = ".$row['user_id'].";");
    }
  }
  $sql = "UPDATE `topup` SET `status` = ".$status.", `amount_receive` = ".$amount." WHERE `requestid` = ".$requestid.";";
  $game->query($sql);
} else {
  header('location: /home');
}

?>