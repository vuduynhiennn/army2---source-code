<?php
  $title = "Quên Chữ Ký - MobiArmy II";
  include './include/head.php';
  include './include/connect.php';
  if (!isset($_SESSION['user_id'])) {
    header('Location: /login');
    exit;
  }
  if ($user['email'] == null || $user['password2'] == '-1') {
    header('Location: /profile');
    exit;
  }
  $error = '';
  $isError = false;
  if (isset($_POST['confirm'])) {
    include './include/sendmail.php';
    $contents = "Chữ ký của bạn là: ".$user['password2'];
    $req = sendMail($user['email'], 'Quên chữ ký', $contents);
    if ($req['status']) {
      $success .= '<span class="invalid-feedback" role="alert" style="color:green"><strong>Chữ ký đã được gửi vào email.</strong></span><br>';
    } else {
      $error = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Lỗi! '.$req['message'].'</strong></span><br>';
    }
  }
?>
                
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Quên Chữ Ký</h4>
    </div>
    <div class="content" style="text-align:center">
      <form action="" method="POST">
        <?php echo $error; echo $success; ?>
        <button name="confirm">Xác nhận</button><br><br>
      <form>
    </div>
  </div>
</div>
<?php
  include './include/end.php';
?>