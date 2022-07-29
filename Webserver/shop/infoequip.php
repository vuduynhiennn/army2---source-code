<?php
if (isset($_POST)) {
  include '../include/config.php';
  include '../include/connect.php';
  $user_id = $_POST['user_id'];
  $nvId = $_POST['nvId'];
  $nameCharacter = array("Gunner", "Miss 6", "Electrician", "King Kong", "Rocketer", "Granos", "Chicky" ,"Tarzan", "Apache", "Magenta");
  $sql = "SELECT `ruongTrangBi` FROM `armymem` WHERE `id` = '".$game->real_escape_string($user_id)."' LIMIT 1;";
  $r = $game->query($sql);
  if ($r->num_rows == 1) {
    $mem = $r->fetch_assoc();
    $ruongTB = json_decode($mem['ruongTrangBi'], true);
    if (count($ruongTB) == 0) {
      echo 'Nhân vật này không có trang bị!';
    } else {
      $num = 0;
      for ($i = 0; $i < count($ruongTB); $i++) {
        $tb = $ruongTB[$i];
        if ($tb['nvId'] != $nvId) {
          continue;
        }
        $num++;
        $sql = "SELECT `name` FROM `equip` WHERE `nv` = ".$tb['nvId']." AND `equipId` = ".$tb['id']." AND `equipType` = ".$tb['equipType']." LIMIT 1;";
        $info = $game->query($sql);
        if ($info->num_rows == 1) {
          $ngoc = "Ngọc: [";
          for ($k = 0; $k < 3; $k++) {
            if ($tb['slot'][$k] != -1) {
              $ngoc .= '<img width="8px", height="8px" src="/public/images/specialitem/'.$tb['slot'][$k].'.png" />';
            }
          }
          $ngoc .= "]";
          $name = $info->fetch_assoc()['name'];
          $vipLevel = $tb['vipLevel'] == 0 ? '' : $tb['vipLevel'];
          $isUse = $tb['isUse'] ? '<a style="color:green">[đã mặc]</a>' : '<a style="color:red">[chưa mặc]</a>';
         echo '<li><b>'.$isUse.$nameCharacter[$tb['nvId']].', '.$name.' '.$vipLevel.', CS: '.json_encode($tb['invAdd']).', %: '.json_encode($tb['percenAdd']).', '.$ngoc.'</b></li>';
        }
      }
      if ($num == 0) {
        echo 'Nhân vật này không có trang bị!';
      }
    }
  } else {
    echo 'Không tìm thấy nhân vật này!';
  }
}
?>