
function getAddress(node) {
  return Bitcoin.payments.p2pkh({ pubkey: node.publicKey }).address;
}

function create_random_btc_xpiv(){
    const mnemonic = bip39.generateMnemonic();
    const seed = bip39.mnemonicToSeedSync(mnemonic);
    const node = Bitcoin.bip32.fromSeed(seed);
    const xpub = node.neutered().toBase58();
    const xprv = node.toBase58();
//create a new xpriv and xpub and retun by alert
//console.log("xpriv:"+xprv+"-xpub:"+xpub);
alert("xpriv:"+xprv+"-xpub:"+xpub);
}
function create_btc_xpub_from_xpriv(xpriv){
//create a xpub from the xpriv and retun it by alert
const node = Bitcoin.bip32.fromBase58(xpriv);
const xpub = node.neutered().toBase58();
alert("xpub:"+xpub);
}
function give_btc_addres_from_xpriv(xpriv,indexnumber){
//give back address based on the index from xpriv
const path = "m/0'/0/0";
const root = Bitcoin.bip32.fromBase58(xpriv);
const node = root.deriveHardened(0).derive(0).derive(indexnumber);
//console.log("publicaddres:"+getAddress(node)+"-privatekey:"+node.toWIF());
alert("publicaddres:"+getAddress(node)+"-privatekey:"+node.toWIF());
}
function sign_a_transaction(privatekey,message){
//give back signed transaction
var keyPair = Bitcoin.ECPair.fromWIF(privatekey);
var privateKey = keyPair.privateKey;
var signature = BitcoinMessage.sign(message, privateKey, keyPair.compressed);
//console.log(signature.toString('base64'));
alert("address:"+signature.toString('base64'));
}
