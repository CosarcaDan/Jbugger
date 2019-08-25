import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  base_url: string = 'http://localhost:8080/jbugs/services/files';

  constructor(private http: HttpClient) {
  }

  public uploadFile(file) {
    this.http.post(this.base_url + '/upload', file).pipe(map(this.extractData));
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }
}
