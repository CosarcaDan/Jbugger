import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  base_url: string = 'http://localhost:8080/jbugs/services/files';

  constructor(private http: HttpClient) {
  }

  /**
   * Sends a post request to upload a file.
   * @param file - the file that has to be uploaded.
   *
   * @return the url of the post request.
   * */
  public uploadFile(file): Observable<any> {
    return this.http.post(this.base_url + '/upload', file);
  }
}
