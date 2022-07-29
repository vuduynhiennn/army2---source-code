<?php
if (isset($_GET['id'])) {
  $title = "Sửa Giá Tiền - MobiArmy II";
  include '../include/head.php';
  include '../include/connect.php';
  if (!isset($_SESSION['user_id'])) {
    header('location: /login');
    exit;
  }
  $id = $_GET['id'];
  $sql = "SELECT `id`, `account_id`, `price` FROM `shop` WHERE `id` = '".$game->real_escape_string($id)."' AND `user_id` = ".$user['user_id']." AND `buy` = 0 LIMIT 1;";
  $acc = $game->query($sql);
  if ($acc->num_rows == 1) {
    $acc = $acc->fetch_assoc();
    if (isset($_POST)) {
      if (isset($_POST['cancel'])) {
        header('location: /shop');
        return;
      }
      if (isset($_POST['confirm'])) {
        $price = $_POST['price'];
        if ($price == null) {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Không được bỏ trống!</strong></span><br>';
        } else if (!preg_match('/^[0-9]+$/', $price)) {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Giá tiền nhập không hợp lệ!</strong></span><br>';
        } else if ($price < 1000 || $price > 1000000) {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Giá bán tối thiểu 1000 và tối đa 1000000!</strong></span><br>';
        } else if ($game->query("UPDATE `shop` SET `price` = '".$game->real_escape_string($price)."' WHERE `id` = '".$acc['id']."' LIMIT 1;")) {
          $error = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Sửa thành công!</strong></span><br>';
        } else {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Sửa thất bại!</strong></span><br>';
        }
      }
    }
  ?>
    <div class="bg-content">
      <div class="content">
        <div class="title">
          <h4>Chỉnh Sửa</h4>
        </div>
        <div class="content" style="text-align:center">
          <? echo $error; ?>
          <form action="" method="POST">
          <span style="margin-left:-135px; font-family: AVO, Arial !important;">Giá</span><br>
          <input value="<? echo $acc['price']; ?>" type="number" name="price" style="margin-top:3px; margin-bottom:5px"><br>
          <button name="confirm">Xác nhận</button><button name="cancel">Hủy</button><br><br>
          <form>
        </div>
      </div>
    </div>
  <? }
  include '../include/end.php';
}
?>