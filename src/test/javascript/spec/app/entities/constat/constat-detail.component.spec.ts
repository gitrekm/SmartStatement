/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SmartStatementTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ConstatDetailComponent } from '../../../../../../main/webapp/app/entities/constat/constat-detail.component';
import { ConstatService } from '../../../../../../main/webapp/app/entities/constat/constat.service';
import { Constat } from '../../../../../../main/webapp/app/entities/constat/constat.model';

describe('Component Tests', () => {

    describe('Constat Management Detail Component', () => {
        let comp: ConstatDetailComponent;
        let fixture: ComponentFixture<ConstatDetailComponent>;
        let service: ConstatService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SmartStatementTestModule],
                declarations: [ConstatDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ConstatService,
                    JhiEventManager
                ]
            }).overrideTemplate(ConstatDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConstatDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConstatService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Constat(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.constat).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
