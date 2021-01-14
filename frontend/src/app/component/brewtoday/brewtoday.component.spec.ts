import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BrewTodayComponent} from './brewtoday.component';

describe('IngredientComponent', () => {
  let component: BrewTodayComponent;
  let fixture: ComponentFixture<BrewTodayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BrewTodayComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrewTodayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
