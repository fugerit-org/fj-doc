const getTypeMessages = list => (
    list.map(item => {
        const obj = {
            ...item,
            text: `${item?.code ? `[${item.code}] - ` : ''}${item.text}`
        }
        return obj;
    })
);

export const getAppMessages = (response) => {
    let ret = null
    if (response?.result) {
        ret = {}
        if (isValuedArray(response.result?.errors)) ret.errors = getTypeMessages(response.result.errors);
        if (isValuedArray(response.result?.warnings)) ret.warnings = getTypeMessages(response.result.warnings);
        if (isValuedArray(response.result?.success)) ret.success = getTypeMessages(response.result.success);
        if (isValuedArray(response.result?.infos)) ret.infos = getTypeMessages(response.result.infos);
    }
    return ret;
};