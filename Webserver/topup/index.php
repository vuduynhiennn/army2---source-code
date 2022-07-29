<?php
$title = "Nạp Tiền - MobiArmy II";
include '../include/head.php';
include '../include/connect.php';
if (!isset($_SESSION['user_id'])) {
    header('Location: /login');
    exit;
}
?>
<div class="bg-content">
  <div class="title">
      <h4>Nạp tiền</h4>
  </div>
  <div class="content" style="text-align:center">
    <span style="margin-left:-115px; font-family: AVO, Arial !important;">Mã thẻ</span><br>
    <input id="code" style="margin-top:3px; margin-bottom:5px"><br>
    <span  style="margin-left:-114px;font-family: AVO, Arial !important;">Mã seri</span><br>
    <input id="serial" style="margin-top:3px; margin-bottom:5px"><br>
    <span  style="margin-left:-110px;font-family: AVO, Arial !important;">Loại thẻ</span><br>
    <select style="margin-top:3px; margin-bottom:5px; margin-left:-19px" id="telco">
      <option value="Viettel" selected>Viettel</option>
      <option value="Vinaphone">Vinaphone</option>
      <option value="Vietnamobile">Vietnamobile</option>
      <option value="Mobifone">Mobifone</option>
      <option value="Zing">Zing</option>
      <option value="Gate">Gate</option>
      <option value="Garena">Garena</option>
    </select><br />
    <span  style="margin-left:-101px;font-family: AVO, Arial !important;">Mệnh giá</span><br>
    <select style="margin-top:3px; margin-bottom:5px; margin-left:-19px" id="amount">
      <option value="10000">10.000</option>
      <option value="20000">20.000</option>
      <option value="50000">50.000</option>
      <option value="100000" selected>100.000</option>
      <option value="200000">200.000</option>
      <option value="500000">500.000</option>
    </select><br />
    <span  style="margin-left:-133px;font-family: AVO, Arial !important;">Loại</span><br>
    <select style="margin-top:3px; margin-bottom:5px; margin-left:-19px" id="naploai">
      <option value="napxu" selected>Nạp xu</option>
      <option value="napluong">Nạp lượng</option>
       <option value="naptien">Nạp tiền</option>
    </select><br />
    <button onclick="topup()" >Nạp</button>
  </div>
    <div class="title">
      <h4>Lịch Sử Nạp Tiền</h4>
  </div>
  <div class="content">
    <table border="0" cellpadding="1" cellspacing="1" style="width:100%; font-size:8px">
    <thead>
      <tr>
        <td style="text-align:center;background:#C0C0C0">Mã giao dịch</td>
          <td style="text-align:center;background:#C0C0C0">Mã thẻ</td>
          <td style="text-align:center;background:#C0C0C0">Mã seri</td>
          <td style="text-align:center;background:#C0C0C0">Mệnh giá</td>
          <td style="text-align:center;background:#C0C0C0">Mệnh giá thực</td>
          <td style="text-align:center;background:#C0C0C0">Nhận</td>
          <td style="text-align:center;background:#C0C0C0">Loại thẻ</td>
          <td style="text-align:center;background:#C0C0C0">Thời gian</td>
           <td style="text-align:center;background:#C0C0C0">Trạng thái</td>
         </tr>
      </thead>
      <tbody>
        <?php
        $sql = "SELECT * FROM `topup` WHERE `user_id` = '".$_SESSION['uid']."';";
        $result = $game->query($sql);
        if ($result->num_rows > 0) {
          while ($card = $result->fetch_assoc()) {
            $t = $card['status'];
            if ($t == 200) {
              $status = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Thành công</strong></span><br>';
            } else if ($t == 201) {
              $status = '<span class="invalid-feedback" role="alert" style="color:green"><strong>Sai mệnh giá</strong></span><br>';
            } else if ($t == 100) {
              $status = '<span class="invalid-feedback" role="alert" style="color:red"><strong>Thẻ sai</strong></span><br>';
            } else {
              $status = '<span class="invalid-feedback" role="alert" style="color:blue"><strong>Đang xử lý</strong></span><br>';
            }
            $txt = '';
            if ($card['type'] == 'napxu') {
              $txt = number_format(((int) $card['amount_receive'] * 10000), 0, '.', '.'). ' xu';
            } else if ($card['type'] == 'napluong') {
              $txt = number_format($card['amount_receive'], 0, '.', '.'). ' lượng';
            } else {
              $txt = number_format($card['amount_receive'], 0, '.', '.'). ' đồng';
            }
            echo '
          <tr>
            <td style="text-align:center;background:white">'.$card['id'].'</th>
            <td style="text-align:center;background:white">'.$card['code'].'</td>
            <td style="text-align:center;background:white">'.$card['seri'].'</td>
            <td style="text-align:center;background:white">'.number_format($card['amount'], 0, '.', '.').'</td>
            <td style="text-align:center;background:white">'.number_format($card['amount_receive'], 0, '.', '.').'</td>
            <td style="text-align:center;background:white">'.$txt.'</td>
            <td style="text-align:center;background:white">'.$card['telco'].'</td>
            <td style="text-align:center;background:white">'.$card['date'].'</td>
            <td style="text-align:center;background:white">'.$status.'</td>
          </tr>';
          }
        } 
        ?>
      </tbody>
    </table>
  </div>
</div>
<?php
include '../include/end.php';
?>