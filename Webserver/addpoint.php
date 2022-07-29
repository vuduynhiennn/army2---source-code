<?php
$title = "Cộng Điểm Nâng Cấp - MobiArmy II";
include './include/head.php';
include './include/connect.php';
if (!isset($_SESSION['user_id'])) {
  header('Location: /login');
  exit;
} 
$nv = json_decode($armymem['NV'.$armymem['NVused']],true);
?>
<div class="bg-content">
  <div class="content">
    <div class="title">
      <h4>Điểm Đã Cộng</h4>
    </div>
      <ul>
        <li><b>Sinh lực: </b> <span id="point0" style="color:red"><?php echo $nv['pointAdd'][0] ?></span></li>
        <li><b>Sức mạnh: </b> <span id="point1" style="color:red"><?php echo $nv['pointAdd'][1] ?></span></li>
        <li><b>Phòng thủ: </b> <span id="point2" style="color:red"><?php echo $nv['pointAdd'][2] ?></span></li>
        <li><b>May mắn: </b> <span id="point3" style="color:red"><?php echo $nv['pointAdd'][3] ?></span></li>
        <li><b>Đồng đội: </b> <span id="point4" style="color:red"><?php echo $nv['pointAdd'][4] ?></span></li>
      </ul>
  </div>
  <div class="content">
    <div class="title">
      <h4>Cộng Điểm: <div id="point"><? echo $nv['point']; ?></div></h4>
    </div>
    <div class="content" style="text-align:center">
      <div id="message"></div>
      <span style="margin-left:-100px; font-family: AVO, Arial !important;">Sinh lực:</span><br>
      <input id="addp0" type="number" value="0" style="margin-top:3px; margin-bottom:5px"><br>
      <span style="margin-left:-86px; font-family: AVO, Arial !important;">Sức mạnh:</span><br>
      <input id="addp1" type="number" value="0" style="margin-top:3px; margin-bottom:5px"><br>
      <span style="margin-left:-87px; font-family: AVO, Arial !important;">Phòng thủ:</span><br>
      <input id="addp2" type="number" value="0" style="margin-top:3px; margin-bottom:5px"><br>
      <span style="margin-left:-89px; font-family: AVO, Arial !important;">May mắn:</span><br>
      <input id="addp3" type="number" value="0" style="margin-top:3px; margin-bottom:5px"><br>
      <span style="margin-left:-92px; font-family: AVO, Arial !important;">Đồng đội:</span><br>
      <input id="addp4" type="number" value="0" style="margin-top:3px; margin-bottom:5px"><br>
      <button onclick="addpoint()">Cộng</button><br><br>
    </div>
  </div>
</div>
	
<?php
include './include/end.php';
?>