declare global {
    export interface Number {
        parseAsDate(): string;
    }
}

Number.prototype.parseAsDate = function (this: number) {
    const date = new Date(this)
    return convertDate(date)
}

function convertDate(inputFormat: Date) {
    const pad = (number: number) => (number < 10) ? '0' + number : number;

    const date = new Date(inputFormat);
    return [pad(date.getDate()), pad(date.getMonth() + 1), date.getFullYear()].join('.')
}

export {}
