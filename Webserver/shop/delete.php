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
        header('location: /shop/quan-ly');
        return;
      }
      if (isset($_POST['confirm'])) {
        if ($game->query("UPDATE `shop` SET `buy` = 2 WHERE `id` = ".$acc['id']." LIMIT 1;")) {
          $error = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Xoá thành công!</strong></span><br>';
        } else {
          $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Xoá thất bại!</strong></span><br>';
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
          <button name="confirm">Xác nhận</button><button name="cancel">Hủy</button><br><br>
          <form>
        </div>
      </div>
    </div>
  <? }
  include '../include/end.php';
}
?>