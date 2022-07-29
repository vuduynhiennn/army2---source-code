<?
  if (isset($_POST)) {
    include '../include/config.php';
    include '../include/connect.php';
    $id = $_POST['id'];
    if ($id != null && preg_match('/^[0-9]+$/', $id)) {
      $sql = "SELECT `shop`.`price`, `user`.`user` FROM `shop`, `user` WHERE `shop`.`account_id` = `user`.`user_id` AND `shop`.`id` = ".$id." AND `shop`.`buy` = 0 LIMIT 1";
      $acc = $game->query($sql);
      if ($acc->num_rows == 1) {
        $acc = $acc->fetch_assoc();
      } else {
        exit(0);
      }
    } else {
      exit(0);
    }
  } else {
    exit(0);
  }
?>
  
  <div class="content">
    <div class="title">
      <h4 style="font-size: 12px">Thanh Toán</h4>
    </div>
    <div style="text-align:center"><br>
      <a style="color: #4e452a" >Bạn muốn mua tài khoản <b style="color:blue"><? echo $acc['user']; ?></b></a><br><br>
      <div id="message"></div>
      <span style="margin-left:-85px; font-family: AVO, Arial !important;">Mật khẩu mới</span><br>
      <input id="password" type="password" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-107px;font-family: AVO, Arial !important;">Email mới </span><br>
      <input id="email" type="email" style="margin-top:3px; margin-bottom:5px"><br>
      <span  style="margin-left:-95px;font-family: AVO, Arial !important;">Chữ ký mới </span><br>
      <input id="signature" style="margin-top:3px; margin-bottom:5px"><br>
      <button onclick="buyAcc(<? echo $id; ?>)">Mua <? echo $acc['price'] ?> đồng
      <button onclick="huy()">Hủy</button> <br>
    </div>
  </div>