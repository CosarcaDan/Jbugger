import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GetBugsComponent } from './get-bugs.component';

describe('GetBugsComponent', () => {
  let component: GetBugsComponent;
  let fixture: ComponentFixture<GetBugsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GetBugsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GetBugsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
