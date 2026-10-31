import CryptoJS from "crypto-js"

const key = CryptoJS.enc.Utf8.parse("abcdef0123456789")

export function encrypt(word) {
    const srcs = CryptoJS.enc.Utf8.parse(word);
    const encrypted = CryptoJS.AES.encrypt(srcs, key, {
        mode : CryptoJS.mode.ECB,
        padding : CryptoJS.pad.Pkcs7
    });
    return encrypted.toString();
}