export class Image {
    id: string;
    url: string;
    name: string;
}

export class Collection {
    public id: number;
    public name: string;
    public description: string;
    public images: any[];

    constructor(id: number, name: string, images:any, description?: string) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.description = description;
	}

    addImage(image:any): void {
		this.images.push(image);
	}

	removeImage(imageId: number, examId:string): void {
		for(var i = 0; i < this.images.length; i++){
            if ((this.images[i].examinationID == examId) && (this.images[i].imageId == imageId)) {
                if (i > -1) {
                    this.images.splice(i, 1);
                }       
            } 
        }
	}

}

