export class Server {
    private url: string = "http://localhost:8080/ExaminationServer/examData/api";
    
	getUrl(): string  {
		return this.url;
	}

}