class Url {
  constructor() {}
  /**
   * 传入对象返回url参数
   * @param {Object} data {属性名：属性值}
   * @return {string}
   */
  getParam(data) {
    let url = "";
    for (let k in data) {
      let value = data[k] !== undefined ? data[k] : "";
      url += `&${k}=${encodeURIComponent(value)}`;
    }
    return url ? url.substring(1) : "";
  }

  /**
   * 将url和参数拼接成完整地址
   * @param {string} url  url地址
   * @param {Json} data json对象
   * @return {string}
   */
  getUrl(url, data) {
    // 看原始url地址中开头是否带?,然后拼接处理好的参数
    return (url += (url.indexOf("?") < 0 ? "?" : "") + this.getParam(data));
  }
}
export default Url;
