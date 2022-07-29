<?php
include "./lib/PHPMailer/src/PHPMailer.php";
include "./lib/PHPMailer/src/Exception.php";
include "./lib/PHPMailer/src/OAuth.php";
include "./lib/PHPMailer/src/POP3.php";
include "./lib/PHPMailer/src/SMTP.php";
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\SMTP;
use PHPMailer\PHPMailer\Exception;

function sendMail($sendTo, $subject, $body) {
  try {
    include "./include/config.php";
    $cfg = $config['smtp'];
    $mail = new PHPMailer(true);
    $mail->SMTPDebug  = $cfg['debug'];
    $mail->setLanguage($cfg['language']);
    $mail->CharSet    = $cfg['charset'];
    $mail->isSMTP();
    $mail->Host       = $cfg['server'];
    $mail->SMTPAuth   = $cfg['auth'];
    $mail->Username   = $cfg['username'];
    $mail->Password   = $cfg['password'];
    $mail->SMTPSecure = $cfg['secure'];
    $mail->Port       = $cfg['port'];
    $mail->setFrom($cfg['from'], $cfg['from_name']);
    $mail->addAddress($sendTo); 
    $mail->addReplyTo($cfg['reply'], $cfg['reply_name']);
    $mail->isHTML($cfg['html']);
    $mail->Subject    = $subject;
    $mail->Body       = $body;
    $mail->send();
    return ['status' => true, 'message' => "Message has been sent"];
  } catch (Exception $e) {
    return ['status' => false, 'message' => $mail->ErrorInfo];
  }
}
?>