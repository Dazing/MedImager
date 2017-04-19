export class Tag {
    id: string;
    searchTerm: string;
}

export class Filter {
    parameter: number 
    value: number

    constructor(parameter: number, value: number){
        this.parameter = parameter;
        this.value = value;
    }
}