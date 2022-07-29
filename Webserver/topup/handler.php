<?php
if (!empty($_POST)){
    include '../include/config.php';
    include '../include/connect.php';
    if (!isset($_SESSION['user_id'])) {
      header('location: /login');
    }
    $rid = (rand(1000000, 9000000) + time());
    $code = $_POST['code'];
    $serial = $_POST['serial'];
    $telco = $_POST['telco'];
    $amount = $_POST['amount'];
    $loainap = $_POST['loainap'];
    $data = array(
        "APIKey" => $config['api']['key'],
        "NetworkCode" => $telco.'',
        "PricesExchange" => $amount . '',
        "NumberCard" => $code.'',
        "SeriCard" => $serial.'',
        "IsFast" => true,
        "RequestId" => $rid . ''
    );
    $d = json_encode($data);
    $curl = curl_init();
    curl_setopt_array($curl, array(
        CURLOPT_URL => $config['api']['url'],
        CURLOPT_RETURNTRANSFER => true,
        CURLOPT_ENCODING => "",
        CURLOPT_MAXREDIRS => 10,
        CURLOPT_TIMEOUT => 0,
        CURLOPT_FOLLOWLOCATION => true,
        CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
        CURLOPT_CUSTOMREQUEST => "POST",
        CURLOPT_POSTFIELDS => $d,
        CURLOPT_HTTPHEADER => array(
            "Content-Type: application/json"
        ) ,
    ));
    $response = curl_exec($curl);
    curl_close($curl);
    $data_rep = json_decode($response, true);
  	if($data_rep['status'] == 200){
      $sql = "INSERT INTO `topup` (`id`, `user_id`, `amount`, `seri`, `code`, `telco`, `type`, `status`, `requestid`) VALUES ('', '".$game->real_escape_string($user['user_id'])."', '".$game->real_escape_string($amount)."', '".$game->real_escape_string($serial)."', '".$game->real_escape_string($code)."', '".$game->real_escape_string($telco)."', '".$game->real_escape_string($loainap)."', '0', '".$rid."');";
      $game->query($sql);
    }
    echo json_encode($data_rep);
} else {
  header('location: /404.html');
}

