import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Bug} from '../../models/bug';
import {Attachment} from '../../models/attachment';

@Injectable({
  providedIn: 'root'
})
export class BugService {
  baseUrl: string = 'http://localhost:8080/jbugs/services/bugs';

  httpOptionsWithoutAuth = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    })
  };

  constructor(private http: HttpClient) {
  }

  /**
   * Sends a post request to add a bug.
   * @param bug - Bug; the bug that has to be added
   * @param attachment - Attachment; the attachment of the bug.
   * @return the url of post request.
   * */
  public getBug(id: number): Observable<Bug> {
    return this.http.get<Bug>(this.baseUrl + '/' + id);
  }

  public add(bug, attachment): Observable<Bug> {
    let body = new HttpParams()
      .set('bug', JSON.stringify(bug))
      .set('attachment', JSON.stringify(attachment));
    return this.http.post<Bug>(this.baseUrl + '/add', body);
  }

  /**
   * Sends a get request for all the bugs.
   *
   * @return the url of get request.
   * */
  public getBugs(): Observable<Array<Bug>> {
    return this.http.get<Array<Bug>>(this.baseUrl, this.httpOptionsWithoutAuth);
  }

  private extractData(res: Response) {
    let body = res;
    return body || {};
  }

  /**
   * Sends a post request to get a bug after a criteria.
   * @param bugCriteria - Bug; the bug criteria to search for.
   *
   * @return the url of post request.
   * */
  public getBugsAfterSearchCriteria(bugCriteria: Bug): Observable<Array<Bug>> {
    return this.http.post<Array<Bug>>(this.baseUrl, bugCriteria);
  }

  /**
   * Sends a delete request delete a bug after id.
   * @param id - number; the bug id that has to be deleted.
   *
   * @return the url of delete request.
   * */
  public deleteBugAfterId(id: number) {
    return this.http.delete<any>(this.baseUrl + '/' + id);
  }

  /**
   * Sends a post request to export the bug in PDF.
   * @param bug - Bug; the bug that has to be exported in PDF.
   *
   * @return the url of post request.
   * */
  public exportInPdf(bug: Bug) {
    return this.http.post<any>(this.baseUrl + '/getPDF', bug);
  }

  /**
   * Sends a put request to edit a bug and its attachments.
   * @param bug - Bug; the bug that has to be edited.
   * @param attachment - Attachment; the bugs attachments.
   *
   * @return the url of put request.
   * */
  public saveEditBug(bug: Bug, attachment) {
    let body = new HttpParams()
      .set('bug', JSON.stringify(bug))
      .set('attachment', JSON.stringify(attachment));
    return this.http.put(this.baseUrl + '/' + bug.id + '/' + 'edit', body);
  }

  /**
   * Sends a post request for all the attachments of a bug.
   * @param bug - Bug; the bug we are looking for attachments.
   *
   * @return the url of post request.
   * */
  public getAttachments(bug: Bug) {
    return this.http.post<Array<Attachment>>(this.baseUrl + '/attachments', bug);
  }

  /**
   * Sends a delete request to delete an attachment of a bug.
   * @param bug - Bug; the bug that has the attachment.
   * @param attachmentId - number; the attachment id that has to be deleted.
   *
   * @return the url of post request.
   * */
  public deleteAttachments(bug: Bug, attachmentId: number) {
    let body = new FormData();
    body.append('bug', JSON.stringify(bug));
    body.append('id', JSON.stringify(attachmentId));

    return this.http.post(this.baseUrl + '/delete-attachment', body);
  }
}
