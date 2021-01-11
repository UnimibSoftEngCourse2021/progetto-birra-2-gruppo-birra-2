import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeFormComponent } from './RecipeForm.component';

describe('FormComponent', () => {
  let component: RecipeFormComponent;
  let fixture: ComponentFixture<RecipeFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecipeFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecipeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should save', () => {
    expect(component).toBeTruthy();
  });
});
